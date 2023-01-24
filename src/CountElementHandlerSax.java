import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler which has the business logic to parse the xml.
 */
public class CountElementHandlerSax extends DefaultHandler {

    private Integer currentSheetRowCount = 0;
    private boolean encounteredData = false;
    private final List<Sheet> sheetList = new ArrayList<>();
    private Sheet sheet = new Sheet();
    private String projectName = "";
    private int projectNameRow = 2;

    public String getProjectName() {
        return projectName;
    }

    public List<Sheet> getSheetList() {
        return sheetList;
    }

    public void setProjectNameRow(int rowNumber) {
        projectNameRow = rowNumber;
    }

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        switch (qName) {
            case "Worksheet" -> {
                sheet = new Sheet();
                sheet.sheetName = attributes.getValue(attributes.getIndex("ss:Name"));
            }
            case "Row" -> {
                currentSheetRowCount++;
                sheet.countOfRows = currentSheetRowCount;
            }
            case "Data" -> {
                encounteredData = true;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (currentSheetRowCount == projectNameRow && sheetList.size() == 0 && encounteredData && projectName.isEmpty()) {
            projectName = String.copyValueOf(ch, start, length).trim();
            encounteredData = false;
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) {
        if (qName.equals("Worksheet")) {
            sheetList.add(sheet);
            sheet = new Sheet();
            currentSheetRowCount = 0;
        }
    }
}