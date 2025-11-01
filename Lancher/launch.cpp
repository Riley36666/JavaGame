#include <windows.h>

int WINAPI WinMain(HINSTANCE hInst, HINSTANCE hPrev, LPSTR lpCmdLine, int nCmdShow) {
    // Path to your JAR (relative or absolute)
    const char* cmd = "javaw -jar ../finishedjar/Game.jar";

    // Start the JAR silently (no console)
    STARTUPINFO si = { sizeof(STARTUPINFO) };
    PROCESS_INFORMATION pi;

    if (CreateProcessA(
        NULL, (LPSTR)cmd, NULL, NULL, FALSE,
        0, NULL, NULL, &si, &pi
    )) {
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
    } else {
        MessageBoxA(NULL, "Failed to launch Game.jar. Make sure Java is installed!", "Error", MB_ICONERROR);
    }

    return 0;
}
