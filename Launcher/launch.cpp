#include <windows.h>
#include <fstream>
#include <filesystem>
#include <string>

int main() {
// load jar
    HRSRC hRes = FindResourceA(NULL, "gamejar", RT_RCDATA);
    if (!hRes) {
        MessageBoxA(NULL, "Failed to find embedded JAR resource.", "Error", MB_ICONERROR);
        return 1;
    }

    HGLOBAL hData = LoadResource(NULL, hRes);
    DWORD size = SizeofResource(NULL, hRes);
    void* data = LockResource(hData);

    // Write it to a temporary file
    std::string tempPath = (std::filesystem::temp_directory_path() / "GameTemp.jar").string();
    std::ofstream out(tempPath, std::ios::binary);
    out.write(static_cast<const char*>(data), size);
    out.close();

    // Build command line for javaw (no console)
    std::string cmd = "javaw -jar \"" + tempPath + "\"";

    // Launch Java silently (no cmd window)
    STARTUPINFOA si = { sizeof(si) };
    si.dwFlags = STARTF_USESHOWWINDOW;
    si.wShowWindow = SW_HIDE;
    PROCESS_INFORMATION pi;

    if (!CreateProcessA(
            NULL,
            cmd.data(),
            NULL, NULL, FALSE,
            CREATE_NO_WINDOW,
            NULL, NULL,
            &si, &pi)) {
        MessageBoxA(NULL, "Failed to launch Java.\nMake sure Java is installed.", "Error", MB_ICONERROR);
        return 1;
    }


    WaitForSingleObject(pi.hProcess, INFINITE);

    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

    // Delete the temp jar after closing
    std::filesystem::remove(tempPath);

    return 0;
}
