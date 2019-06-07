package com.mars.core.util;

import com.diffblue.deeptestutils.mock.DTUMemberMatcher;
import com.alibaba.fastjson.JSON;
import org.ho.yaml.Yaml;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*"})
public class FileUtilTests {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @PrepareForTest({FileUtil.class})
    @Test
    public void testReadFileString1() throws Exception {
        InputStreamReader inputStreamReader =
                PowerMockito.mock(InputStreamReader.class);
        BufferedReader bufferedReader =
                new BufferedReader(new StringReader("line1\nline2\nline3"));
        PowerMockito.whenNew(InputStreamReader.class).withAnyArguments()
                .thenReturn(inputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withAnyArguments()
                .thenReturn(bufferedReader);

        Assert.assertEquals("line1line2line3", FileUtil.readFileString("foo"));
    }

    @Test
    public void testReadFileString2() {
        FileUtil.readFileString(null);
    }

    @PrepareForTest({JSON.class, Yaml.class, FileUtil.class})
    @Test
    public void testReadYml() throws Exception {
        PowerMockito.mockStatic(Yaml.class);
        PowerMockito.mockStatic(JSON.class);

        Method toJSONStringMethod = DTUMemberMatcher.method(
                JSON.class, "toJSONString", Object.class);
        PowerMockito.doReturn("foo")
                .when(JSON.class, toJSONStringMethod)
                .withArguments(or(isA(Object.class), isNull(Object.class)));

        Assert.assertEquals("foo", FileUtil.readYml("a/b/c"));
    }

    @Test
    public void testReadYmlException() throws Exception {
        thrown.expect(NullPointerException.class);
        FileUtil.readYml(null);
    }

    @PrepareForTest({FileInputStream.class, FileUtil.class, File.class})
    @Test
    public void testGetFileToByte1() throws Exception {
        File file = PowerMockito.mock(File.class);
        FileInputStream fileInputStream =
                PowerMockito.mock(FileInputStream.class);
        PowerMockito.when(fileInputStream.read(or(isA(byte[].class),
                isNull(byte[].class)))).thenReturn(1).thenReturn(-1);
        PowerMockito.whenNew(FileInputStream.class)
                .withAnyArguments().thenReturn(fileInputStream);

        Assert.assertArrayEquals(new byte[]{(byte) 0},
                FileUtil.getFileToByte(file));
    }

    @Test
    public void testGetFileToByte2() {
        Assert.assertNull(FileUtil.getFileToByte(new File("foo.txt")));
    }

    @Test
    public void testGetInputStreamToByte1() {
        byte[] bytes = new byte[]{-74, -10, -10, -10, 15, -9, -73, -9, -10};
        ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);

        Assert.assertArrayEquals(bytes,
                FileUtil.getInputStreamToByte(inStream));
    }

    @Test
    public void testGetInputStreamToByte2() {
        Assert.assertNull(FileUtil.getInputStreamToByte(null));
    }

    @PrepareForTest({FileUtil.class, ImageIO.class})
    @Test
    public void testGetBufferedImageToByte1() {
        PowerMockito.mockStatic(ImageIO.class);
        BufferedImage bufferedImage = PowerMockito.mock(BufferedImage.class);

        Assert.assertArrayEquals(new byte[]{},
                FileUtil.getBufferedImageToByte(bufferedImage));
    }

    @Test
    public void testGetBufferedImageToByte2() {
        Assert.assertNull(FileUtil.getBufferedImageToByte(null));
    }
}
