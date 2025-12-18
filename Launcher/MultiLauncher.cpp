#include <windows.h>
#include <string>
#include <fstream>
#include <filesystem>
#include <dwmapi.h>
#pragma comment(lib, "dwmapi.lib")

LRESULT CALLBACK WindowProc(HWND, UINT, WPARAM, LPARAM);
void LaunchEmbeddedJar(const char* resourceName);
void DrawGradientBackground(HDC hdc, RECT rc, COLORREF top, COLORREF bottom);

#define ID_BUTTON_PLATFORMER 1001
#define ID_BUTTON_TODO       1002

HINSTANCE g_hInstance;

// =========================
//      ENTRY POINT
// =========================
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE, LPSTR, int nCmdShow) {
    g_hInstance = hInstance;

    WNDCLASSA wc{};
    wc.lpfnWndProc = WindowProc;
    wc.hInstance = hInstance;
    wc.lpszClassName = "MultiLauncherWindow";
    wc.hCursor = LoadCursor(NULL, IDC_ARROW);
    wc.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(1));

    RegisterClassA(&wc);

    HWND hwnd = CreateWindowExA(
        WS_EX_APPWINDOW,
        wc.lpszClassName,
        "MultiLauncher",
        WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX,
        CW_USEDEFAULT, CW_USEDEFAULT, 800, 720,
        NULL, NULL, hInstance, NULL
    );

    ShowWindow(hwnd, nCmdShow);
    UpdateWindow(hwnd);

    MSG msg{};
    while (GetMessageA(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessageA(&msg);
    }
    return 0;
}

// =========================
//      BACKGROUND
// =========================
void DrawGradientBackground(HDC hdc, RECT rc, COLORREF top, COLORREF bottom) {
    TRIVERTEX v[2];

    v[0].x = rc.left;
    v[0].y = rc.top;
    v[0].Red   = static_cast<COLOR16>(GetRValue(top) << 8);
    v[0].Green = static_cast<COLOR16>(GetGValue(top) << 8);
    v[0].Blue  = static_cast<COLOR16>(GetBValue(top) << 8);
    v[0].Alpha = 0;

    v[1].x = rc.right;
    v[1].y = rc.bottom;
    v[1].Red   = static_cast<COLOR16>(GetRValue(bottom) << 8);
    v[1].Green = static_cast<COLOR16>(GetGValue(bottom) << 8);
    v[1].Blue  = static_cast<COLOR16>(GetBValue(bottom) << 8);
    v[1].Alpha = 0;

    GRADIENT_RECT g = { 0, 1 };
    GradientFill(hdc, v, 2, &g, 1, GRADIENT_FILL_RECT_V);
}

// =========================
//      MAIN WINDOW
// =========================
LRESULT CALLBACK WindowProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    static HFONT hTitleFont, hButtonFont;
    static HWND hTitle, hSubtitle, hGameBtn, hTodoBtn;

    switch (msg) {
    case WM_CREATE:
        hTitleFont = CreateFontA(42, 0, 0, 0, FW_EXTRABOLD, FALSE, FALSE, FALSE,
            DEFAULT_CHARSET, OUT_OUTLINE_PRECIS, CLIP_DEFAULT_PRECIS,
            CLEARTYPE_QUALITY, VARIABLE_PITCH, "Segoe UI");

        hButtonFont = CreateFontA(22, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE,
            DEFAULT_CHARSET, OUT_OUTLINE_PRECIS, CLIP_DEFAULT_PRECIS,
            CLEARTYPE_QUALITY, VARIABLE_PITCH, "Segoe UI");

        hTitle = CreateWindowA("static", "MultiLauncher",
            WS_VISIBLE | WS_CHILD | SS_CENTER,
            200, 80, 400, 60,
            hwnd, NULL, g_hInstance, NULL);

        hSubtitle = CreateWindowA("static", "Choose what you want to launch",
            WS_VISIBLE | WS_CHILD | SS_CENTER,
            220, 140, 360, 30,
            hwnd, NULL, g_hInstance, NULL);

        hGameBtn = CreateWindowA("button", "Play Platformer",
            WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON,
            300, 260, 200, 55,
            hwnd, (HMENU)ID_BUTTON_PLATFORMER, g_hInstance, NULL);

        hTodoBtn = CreateWindowA("button", "Open Todo App",
            WS_VISIBLE | WS_CHILD | BS_PUSHBUTTON,
            300, 340, 200, 55,
            hwnd, (HMENU)ID_BUTTON_TODO, g_hInstance, NULL);

        SendMessageA(hTitle, WM_SETFONT, (WPARAM)hTitleFont, TRUE);
        SendMessageA(hSubtitle, WM_SETFONT, (WPARAM)hButtonFont, TRUE);
        SendMessageA(hGameBtn, WM_SETFONT, (WPARAM)hButtonFont, TRUE);
        SendMessageA(hTodoBtn, WM_SETFONT, (WPARAM)hButtonFont, TRUE);
        return 0;

    case WM_CTLCOLORSTATIC:
    case WM_CTLCOLORBTN: {
        HDC hdc = (HDC)wParam;
        SetBkMode(hdc, TRANSPARENT);
        SetTextColor(hdc, RGB(240, 240, 255));
        return (LRESULT)GetStockObject(NULL_BRUSH);
    }

    case WM_PAINT: {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hwnd, &ps);
        RECT rc;
        GetClientRect(hwnd, &rc);
        DrawGradientBackground(hdc, rc, RGB(15, 20, 40), RGB(40, 60, 120));
        EndPaint(hwnd, &ps);
        return 0;
    }

    case WM_COMMAND:
        switch (LOWORD(wParam)) {
        case ID_BUTTON_PLATFORMER:
            DestroyWindow(hwnd);
            LaunchEmbeddedJar("gamejar");
            break;

        case ID_BUTTON_TODO:
            DestroyWindow(hwnd);
            LaunchEmbeddedJar("todojar");
            break;
        }
        return 0;

    case WM_DESTROY:
        PostQuitMessage(0);
        return 0;
    }

    return DefWindowProcA(hwnd, msg, wParam, lParam);
}

// =========================
//     JAR EXECUTION
// =========================
void LaunchEmbeddedJar(const char* resourceName) {
    HRSRC hRes = FindResourceA(NULL, resourceName, RT_RCDATA);
    if (!hRes) {
        MessageBoxA(NULL, "Embedded JAR not found.", "Launcher Error", MB_ICONERROR);
        return;
    }

    HGLOBAL hData = LoadResource(NULL, hRes);
    DWORD size = SizeofResource(NULL, hRes);
    void* data = LockResource(hData);

    std::string tempJar = (std::filesystem::temp_directory_path() / resourceName).string() + ".jar";
    std::ofstream out(tempJar, std::ios::binary);
    out.write((const char*)data, size);
    out.close();

    std::string cmd = "javaw -jar \"" + tempJar + "\"";

    STARTUPINFOA si{ sizeof(si) };
    PROCESS_INFORMATION pi{};

    if (!CreateProcessA(NULL, cmd.data(), NULL, NULL, FALSE,
        CREATE_NO_WINDOW, NULL, NULL, &si, &pi)) {

        MessageBoxA(NULL, "Java not found.\nPlease install Java.", "Launcher Error", MB_ICONERROR);
        return;
    }

    WaitForSingleObject(pi.hProcess, INFINITE);
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);
    std::filesystem::remove(tempJar);
}
