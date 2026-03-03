package LeHuynhThuan.demo.repository;

import LeHuynhThuan.demo.entity.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findByUserId(String userId);
}
