#include <windows.h>
#include <fstream>
#include <filesystem>
#include <string>

int main() {
    // 1️⃣ Load the embedded JAR resource
    HRSRC hRes = FindResourceA(NULL, "gamejar", RT_RCDATA);
    if (!hRes) {
        MessageBoxA(NULL, "Failed to find embedded JAR resource.", "Error", MB_ICONERROR);
        return 1;
    }

    HGLOBAL hData = LoadResource(NULL, hRes);
    DWORD size = SizeofResource(NULL, hRes);
    void* data = LockResource(hData);

    // 2️⃣ Write it to a temporary file
    std::string tempPath = (std::filesystem::temp_directory_path() / "GameTemp.jar").string();
    std::ofstream out(tempPath, std::ios::binary);
    out.write(static_cast<const char*>(data), size);
    out.close();

    // 3️⃣ Build command line for javaw (no console)
    std::string cmd = "javaw -jar \"" + tempPath + "\"";

    // 4️⃣ Launch Java silently (no cmd window)
    STARTUPINFOA si = { sizeof(si) };
    si.dwFlags = STARTF_USESHOWWINDOW;
    si.wShowWindow = SW_HIDE; // hide any potential window
    PROCESS_INFORMATION pi;

    if (!CreateProcessA(
            NULL,
            cmd.data(),  // command line
            NULL, NULL, FALSE,
            CREATE_NO_WINDOW,  // prevents any console window
            NULL, NULL,
            &si, &pi)) {
        MessageBoxA(NULL, "Failed to launch Java.\nMake sure Java is installed.", "Error", MB_ICONERROR);
        return 1;
    }

    // 5️⃣ Optional: wait for the game to close
    WaitForSingleObject(pi.hProcess, INFINITE);

    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

    // 6️⃣ Delete the temp jar after closing
    std::filesystem::remove(tempPath);

    return 0;
}
