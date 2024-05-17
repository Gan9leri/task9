import JsonModels.Fridge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.codeborne.pdftest.PDF;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.json.Json;
import java.util.List;
import java.util.Objects;

public class FileVerification {
    private final ClassLoader cl = FileVerification.class.getClassLoader();
    private final Json json = new Json();
    @DisplayName("Тест корректности данных в файлах архива")
    @Test
    void zipFileParsingTest() throws Exception {
        try(ZipInputStream zip_stream = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("task9.zip")))) {
            ZipEntry entry;
            while ((entry = zip_stream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    XLS xls = new XLS(zip_stream);
                    String check = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue(),
                            full_name = xls.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue(),
                            phone = xls.excel.getSheetAt(0).getRow(0).getCell(2).getStringCellValue(),
                            user_check = String.valueOf((int) xls.excel.getSheetAt(0).getRow(1).getCell(0).getNumericCellValue()),
                            user_full_name = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue(),
                            user_phone = String.valueOf((int) xls.excel.getSheetAt(0).getRow(1).getCell(2).getNumericCellValue());
                    Assertions.assertEquals("Счет", check);
                    Assertions.assertEquals("ФИО", full_name);
                    Assertions.assertEquals("Телефон", phone);
                    Assertions.assertEquals("12345", user_check);
                    Assertions.assertEquals("Овсянников", user_full_name);
                    Assertions.assertEquals("12345", user_phone);
                }
                if (entry.getName().endsWith(".csv")) {
                    CSVReader csv = new CSVReader(new InputStreamReader(zip_stream));
                    List<String[]> Arr = csv.readAll();
                    Assertions.assertArrayEquals(new String[]{"Alex", " Smith"}, Arr.get(0));
                }
                if (entry.getName().endsWith(".pdf")) {
                    PDF pdf = new PDF(zip_stream);
                    Assertions.assertEquals("Пример pdf", pdf.text.trim());
                }
            }
        }
    }
    @DisplayName("Тест корректности данных в json файле")
    @Test
    void jsonFileVerification() throws Exception{
        try(InputStream is = cl.getResourceAsStream("task9.json")){
            ObjectMapper mapper = new ObjectMapper();
            Fridge fridge = mapper.readValue(is, Fridge.class);
            Assertions.assertEquals("fridge", fridge.getTitle());
            Assertions.assertEquals(true, fridge.getSelfDefrosting());
            Assertions.assertEquals(2, fridge.getCapacity().getCountDoor());
            Assertions.assertEquals(8, fridge.getCapacity().getCountShelf());
            Assertions.assertEquals(2, fridge.getCapacity().getCountDoor());
        }
    }
}