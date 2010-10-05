package org.ixcode.golog;

import org.junit.*;

import java.io.*;
import java.net.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class FetchSomeGoArtifactsIntegrationTest {

    @Test
    public void getsSomeArtifactsFromAGoServer() {
        String response = getArtifactFrom("cruise-server");

// assertThat(response, containsString("error"));
          // need to login!
    }

    @Test
    public void testReadingInputStream() throws IOException {
        String input = "BLah blah\nblah";

        String output = readInputStream(new ByteArrayInputStream(input.getBytes("UTF-8")));
        assertThat(output, is(input));
    }

    private static String getArtifactFrom(String url) {
        try {
            return getArtifactFrom(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getArtifactFrom(URL url) {
        URLConnection urlConnection = openConnectionTo(url);
        InputStream in = null;
        try {
            in = urlConnection.getInputStream();
            return readInputStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            tryToClose(in);
        }
    }

    private static String readInputStream(InputStream in) throws IOException {
        byte[] buffer = new byte[255];

        StringBuilder sb = new StringBuilder();
        int bytesRead = in.read(buffer);
        while (bytesRead != -1) {
            sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
            bytesRead = in.read(buffer);
        }

        return sb.toString();
    }


    private static void tryToClose(InputStream in) {

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static URLConnection openConnectionTo(URL url) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("someproxy.server", 8080));
        try {
            return url.openConnection(proxy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}