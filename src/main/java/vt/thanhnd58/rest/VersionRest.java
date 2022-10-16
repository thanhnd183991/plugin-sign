package vt.thanhnd58.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import vt.thanhnd58.constant.PluginConstant;
import vt.thanhnd58.dto.VersionDTO;
import vt.thanhnd58.enums.TypeFile;
import vt.thanhnd58.os.OS;
import vt.thanhnd58.os.OSName;
import vt.thanhnd58.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

public class VersionRest {
    private static final String URL_VERSION = "http://localhost:8080/api/file";

    private static RestTemplate restTemplate = new RestTemplate();


    public static VersionDTO checkVersion() {
        Properties projectProps = FileUtils.getPropFileFromFolderContainApp(PluginConstant.PROJECT_PROPS);
        OSName osname = OS.getOS();
        String version = projectProps.getProperty("yourVersion");

        String url = URL_VERSION + "/check-version?os=" + osname.toString();
        ResponseEntity<VersionDTO> response = restTemplate.getForEntity(url, VersionDTO.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            VersionDTO versionDTO = response.getBody();
            versionDTO.setYourVersion(version);
            return versionDTO;
        } else {
            return null;
        }
    }

    public static boolean downloadJarFileWithoutResume(String version, TypeFile typeFile) {
        OSName osName = OS.getOS();
        String url = null;
        if (version == null) {
            url = URL_VERSION + "/download-last-version?os=" + osName.toString() + "&typeFile=" + typeFile.toString();
        } else {
            url = URL_VERSION + "/download-version?os=" + osName.toString() + "&version=" + version + "&typeFile=" + typeFile.toString();
        }
        System.out.println("url download file: " + url);
//        File file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
//            String filePath  = FileUtils.getFilePathInFolderContainApp(TEMP_FILE);
//            File ret = new File(filePath);
//            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
//            return ret;
//        });
//        return file;

        try {
            String filePath = FileUtils.getFilePathInFolderContainApp(typeFile == TypeFile.HDDT ? PluginConstant.TEMP_APP_JAR_FILE : PluginConstant.TEMP_UPDATER_JAR_FILE);

            ReadableByteChannel rbc = Channels.newChannel((new URL(url)).openStream());

            FileOutputStream fos = new FileOutputStream(filePath);

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

//    public File downloadJarFileResume() {
//        OSName osName = OS.getOS();
//        String url = URL_VERSION + "/download-last-version?os=" + osName.toString();
//        HttpHeaders headers = restTemplate.headForHeaders(url);
//
//        restTemplate.execute(
//                url,
//                HttpMethod.GET,
//                clientHttpRequest -> clientHttpRequest.getHeaders().set(
//                        "Range",
//                        String.format("bytes=%d-", file.length() )),
//                clientHttpResponse -> {
//                    StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(file, true));
//                    return file;
//                });
//
//
//    }
}
