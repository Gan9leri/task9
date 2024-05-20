import models.Fridge;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.codeborne.pdftest.PDF;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import static org.assertj.core.api.Assertions.assertThat;

public class FileVerificationTests {
    private final ClassLoader cl = FileVerificationTests.class.getClassLoader();

    @DisplayName("Тест файла xlsx из архива zip")
    @Test
    void xlsxFileVerificationTest(){
        try(ZipInputStream zip_stream = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("task9.zip")))) {
            ZipEntry entry;
            while ((entry = zip_stream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    XLS xls = new XLS(zip_stream);
                    String check = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue(),
                            fullName = xls.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue(),
                            phone = xls.excel.getSheetAt(0).getRow(0).getCell(2).getStringCellValue(),
                            userCheck = String.valueOf((int) xls.excel.getSheetAt(0).getRow(1).getCell(0).getNumericCellValue()),
                            userFullName = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue(),
                            userPhone = String.valueOf((int) xls.excel.getSheetAt(0).getRow(1).getCell(2).getNumericCellValue());
                    assertThat(check).isEqualTo("Счет");
                    assertThat(fullName).isEqualTo("ФИО");
                    assertThat(phone).isEqualTo("Телефон");
                    assertThat(userCheck).isEqualTo("12345");
                    assertThat(userFullName).isEqualTo("Овсянников");
                    assertThat(userPhone).isEqualTo("12345");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Тест файла csv из архива zip")
    @Test
    void csvFileVerificationTest(){
        try(ZipInputStream zip_stream = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("task9.zip")))) {
            ZipEntry entry;
            while ((entry = zip_stream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")) {
                    CSVReader csv = new CSVReader(new InputStreamReader(zip_stream));
                    List<String[]> fullName = csv.readAll();
                    assertThat(fullName.get(0)).isEqualTo(new String[]{"Alex", " Smith"});
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Тест файла pdf из архива zip")
    @Test
    void pdfFileVerificationTest(){
        try(ZipInputStream zip_stream = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("task9.zip")))) {
            ZipEntry entry;
            while ((entry = zip_stream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")) {
                    PDF pdf = new PDF(zip_stream);
                    assertThat(pdf.text.trim()).isEqualTo("Пример pdf");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Тест файла json")
    @Test
    void jsonFileVerification() throws Exception{
        try(InputStream is = cl.getResourceAsStream("task9.json")){
            ObjectMapper mapper = new ObjectMapper();
            Fridge fridge = mapper.readValue(is, Fridge.class);
            assertThat(fridge.getTitle()).isEqualTo("fridge");
            assertThat(fridge.getSelfDefrosting()).isEqualTo(true);
            assertThat(fridge.getCapacity().getCountFreezer()).isEqualTo(2);
            assertThat(fridge.getCapacity().getCountShelf()).isEqualTo(8);
            assertThat(fridge.getCapacity().getCountDoor()).isEqualTo(2);
        }
    }
}