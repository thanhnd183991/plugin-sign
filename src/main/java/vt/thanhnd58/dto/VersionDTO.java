package vt.thanhnd58.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vt.thanhnd58.os.OSName;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionDTO {
    private OSName osName;
    private String yourVersion;
    private String newVersion;
    private boolean required;
    private long size;
    private boolean updater;
}
