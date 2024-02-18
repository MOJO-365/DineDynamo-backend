package com.dinedynamo.services;

import com.dinedynamo.collections.Table;
import com.dinedynamo.repositories.TableRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.IOException;
import java.util.*;

import java.util.Arrays;



@Service
public class TableService
{
    @Autowired
    TableRepository tableRepository;

    @Autowired
    QRCodeService genetateQRCodeService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    MongoClient mongoClient;



    public final MongoTemplate mongoTemplate;

    @Autowired
    public TableService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public Table findById(String tableId){
        return tableRepository.findById(tableId).orElse(null);
    }



    // This save method will take table details, also generate QR for that table and store it in database
    public Table save(Table table) throws IOException {

        if(table == null){
            return null;
        }


        if (!isTableCategoryAndNameUnique(table.getRestaurantId(), table.getTableCategory(), table.getTableName())) {
            mongoTemplate.save(table);
            //tableRepository.save(table);

        } else {

            throw new RuntimeException("Table name must be unique within the category for a specific restaurant.");
        }




        System.out.println("Priting: "+table.getPublicIdOfQRImage());
        //table = tableRepository.findById(table.getTableId()).orElse(null);
        //This means the QR code for table has not been generated even for once
        if(table.getPublicIdOfQRImage() == null){
            byte[] qrByteArray = genetateQRCodeService.generateQrCodeImage(table.getTableId(), 200, 200);

            Map mapOfImage = cloudinaryService.uploadImageOnCloudinary(qrByteArray);

            table.setTableQRURL((String) mapOfImage.get("url"));
            table.setPublicIdOfQRImage((String) mapOfImage.get("public_id"));
        }

        tableRepository.save(table);
        return table;

    }


    /**
     *
     * @param table
     * @return deleted table
     * @throws IOException
     * This method deletes the corresponding table qr from cloudinary and the table from database
     */
    public Table delete(Table table) throws IOException {

        cloudinaryService.deleteImageFromCloudinary(table.getPublicIdOfQRImage());

        tableRepository.delete(table);

        return table;
    }




    public List<Document> getGroupByTables(String restaurantId){

        MongoDatabase database = mongoClient.getDatabase("cluster0");
        MongoCollection<org.bson.Document> collection = database.getCollection("tables");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                new BasicDBObject("$match", new BasicDBObject("restaurantId", restaurantId)),
                new BasicDBObject("$group", new BasicDBObject("_id", "$tableCategory")
                        .append("tables", new BasicDBObject("$push", "$$ROOT")))));




        List<Document> resultList = new ArrayList<>();
        result.into(resultList);

        return resultList;

    }


    public boolean isTableCategoryAndNameUnique(String restaurantId, String tableCategory, String tableName) {
        // Query to check uniqueness in the database
        return mongoTemplate.exists(
                Query.query(Criteria.where("restaurantId").is(restaurantId)
                        .and("tableCategory").is(tableCategory)
                        .and("tableName").is(tableName)),
                Table.class
        );
    }

}



//    public List<Table> bestMergeTables(int requestedCapacity){
//
//        //Here find all tables by restaurantId, not findAll()
//        List<Table> allTables = tableRepository.findAll();
//
//        // Sort tables by remaining capacity in descending order
//        allTables.sort(Comparator.comparingInt(Table::getCapacity).reversed());
//
//        int n = allTables.size();
//        int[][] dp = new int[n + 1][requestedCapacity + 1];
//
//        for (int i = 1; i <= n; i++) {
//            Table currentTable = allTables.get(i - 1);
//            for (int j = 0; j <= requestedCapacity; j++) {
//                if (currentTable.getCapacity() <= j) {
//                    // Include the current table and check the remaining capacity
//                    int includeTable = currentTable.getCapacity() + dp[i - 1][j - currentTable.getCapacity()];
//                    // Exclude the current table
//                    int excludeTable = dp[i - 1][j];
//                    dp[i][j] = Math.max(includeTable, excludeTable);
//                } else {
//                    // If the current table cannot be included, exclude it
//                    dp[i][j] = dp[i - 1][j];
//                }
//            }
//        }
//
//        int remainingCapacity = requestedCapacity;
//        List<Table> bestMergedTables = new ArrayList<>();
//
//        // Reconstruct the best combination
//        for (int i = n; i > 0 && remainingCapacity > 0; i--) {
//            if (dp[i][remainingCapacity] != dp[i - 1][remainingCapacity]) {
//                Table selectedTable = allTables.get(i - 1);
//                bestMergedTables.add(selectedTable);
//                remainingCapacity -= selectedTable.getCapacity();
//            }
//        }
//
//        // You may want to handle cases where the requested capacity cannot be met
//        if (bestMergedTables.isEmpty() || remainingCapacity > 0) {
//            // Handle the case where the requested capacity cannot be met with the available tables
//            // You might want to throw an exception or handle it based on your business rules
//            // For simplicity, we can clear the merged tables in case of failure
//            bestMergedTables = new ArrayList<>();
//        }
//
//        // Reverse the list to get the merged tables in the desired order
//        // (i.e., with the minimum wasted capacity at the end)
//        List<Table> reversedList = new ArrayList<>(bestMergedTables);
//        java.util.Collections.reverse(reversedList);
//        return reversedList;
//
//    }
//