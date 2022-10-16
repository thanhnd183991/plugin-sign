package vt.thanhnd58.os;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OSTest {

    @Test
    void getOS() {
        assertEquals(OSName.WINDOWS, OS.getOS());
    }
}