package com.kochetkov;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTests {

    @Test
    public void downloadFileTest() throws Exception {
        open("https://github.com/IlyaKoch/github-tests/blob/main/README.md");
        File download = $("#raw-url").download();
        String result;
        try (InputStream is = new FileInputStream(download)) {
            result = new String(is.readAllBytes(),"UTF-8");
        }
        Assertions.assertTrue(result.contains("Technology stack"));
    }

    @Test
    public void uploadFileTest() {
        open("https://the-internet.herokuapp.com/upload");
        $("#file-upload").uploadFromClasspath("example.txt");
        $("#file-submit").click();
        $("#uploaded-files").
                shouldHave(
                        Condition.text("example.txt")
                );
    }

    @Test
    public void downloadPdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File download = $x("//a[text()= 'PDF download']").download();
        PDF parsed = new PDF(download);
        assertThat(parsed.author).contains("Marc Philipp");
    }

    @Test
    public void XlsTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("123.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(2).getCell(4).getStringCellValue()).isEqualTo("Илья К");
        }
    }
}