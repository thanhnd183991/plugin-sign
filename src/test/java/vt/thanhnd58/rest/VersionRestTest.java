package vt.thanhnd58.rest;

import org.junit.jupiter.api.Test;
import vt.thanhnd58.dto.VersionDTO;
import vt.thanhnd58.enums.TypeFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class VersionRestTest {

    @Test
    void checkVersion() {
        VersionDTO rs = VersionRest.checkVersion();
    }

    @Test
    void downloadJarFileWithoutResume() {
        boolean file = VersionRest.downloadJarFileWithoutResume("1.0.2", TypeFile.UPDATER);
    }

    @Test
    void testCheckVersion() {
        var rs = VersionRest.checkVersion();
    }
}