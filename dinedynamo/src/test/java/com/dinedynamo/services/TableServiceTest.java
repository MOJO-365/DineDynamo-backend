package com.dinedynamo.services;
import com.dinedynamo.collections.table_collections.Table;
import com.dinedynamo.repositories.TableRepository;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private QRCodeService generateQRCodeService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private Table table;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_ValidTableId() {
        String tableId = "111";
        when(tableRepository.findById(tableId)).thenReturn(java.util.Optional.of(table));

        Table result = tableService.findById(tableId);

        assertEquals(table, result);
    }

    @Test
    void testFindById_InvalidTableId() {
        String tableId = "456";
        when(tableRepository.findById(tableId)).thenReturn(java.util.Optional.empty());

        Table result = tableService.findById(tableId);

        assertNull(result);
    }

    @Test
    void testSave_ValidTable() throws IOException {
        when(table.getPublicIdOfQRImage()).thenReturn(null);
        when(generateQRCodeService.generateQrCodeImage(any(), anyInt(), anyInt())).thenReturn(new byte[0]);
        when(cloudinaryService.uploadImageOnCloudinary(any())).thenReturn(new java.util.HashMap<>());

        Table result = tableService.save(table);

        assertNotNull(result);
        verify(tableRepository, times(2)).save(table);
    }

    @Test
    void testSave_TableWithQRImage() throws IOException {
        when(table.getPublicIdOfQRImage()).thenReturn("public_id");

        Table result = tableService.save(table);

        assertNotNull(result);
        verify(tableRepository, times(2)).save(table);
    }

    @Test
    void testDelete_ValidTable() throws IOException {
        when(table.getPublicIdOfQRImage()).thenReturn("public_id");

        Table result = tableService.delete(table);

        assertNotNull(result);
        verify(cloudinaryService, times(1)).deleteImageFromCloudinary("public_id");
        verify(tableRepository, times(1)).delete(table);
    }

//    @Test
//    void testGetGroupByTables() {
//        List<Document> expectedResult = new ArrayList<>();
//        when(mongoClient.getDatabase("cluster0")).thenReturn(null);
//
//        List<Document> result = tableService.getGroupByTables("65c444acd1aa33307bd6e918");
//
//        assertEquals(expectedResult, result);
//    }
//    @Test
//    void testGetGroupByTables() {
//        // Arrange
//        MongoDatabase mockDatabase = mock(MongoDatabase.class);
//        when(mongoClient.getDatabase("cluster0")).thenReturn(mockDatabase);
//
//        MongoCollection<Document> mockCollection = mock(MongoCollection.class);
//        when(mockDatabase.getCollection("tables", Document.class)).thenReturn(mockCollection);
//
//        AggregateIterable<Document> mockResult = mock(AggregateIterable.class);
//        when(mockCollection.aggregate(anyList())).thenReturn(mockResult);
//
//        List<Document> expectedResult = new ArrayList<>();
//        when(mockResult.into(anyList())).thenReturn(expectedResult);
//
//        // Act
//        List<Document> result = tableService.getGroupByTables("65c444acd1aa33307bd6e918");
//
//        // Assert
//        assertEquals(expectedResult, result);
//
//        // Verify interactions
//        verify(mongoClient).getDatabase("cluster0");
//        verify(mockDatabase).getCollection("tables", Document.class);
//        verify(mockCollection).aggregate(anyList());
//        verify(mockResult).into(anyList());
//    }


}
