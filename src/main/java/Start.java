import org.elasticsearch.client.transport.TransportClient;

import java.util.List;

public class Start {
    private static TransportClient client = EsUtil.getClient("elasticsearch", "localhost", 9300);
    private static String mappingStr = DealFile.readFile2String("src/main/resources/mapping.txt");
    private static String rootPath = DealFile.readFile2String("src/main/resources/localRootPath.txt");

    private static void createIndex(){
        EsUtil.createIndex(client,"experimentdataset", mappingStr);
    }

    private static void insertData(){
        List folder = DealFile.getAllFileName(rootPath);
        for (int i = 0; i<folder.size(); i++){
            String file = folder.get(i).toString();
            String currentFilePath = rootPath+"/"+file;
            String currentFileContent = DealFile.readFile2String(currentFilePath);
            fileType obj = new fileType(file,currentFileContent);
            EsUtil.insertData(client, "experimentdataset", "file", obj, i+"");
        }
    }
    public static void main(String[] args) {
        createIndex();
        insertData();
    }

}
