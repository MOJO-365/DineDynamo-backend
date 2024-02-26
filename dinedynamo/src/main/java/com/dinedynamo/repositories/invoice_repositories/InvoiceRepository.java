package com.dinedynamo.repositories.invoice_repositories;

import com.dinedynamo.collections.invoice_collections.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice,String> {


}
