package LeHuynhThuan.demo.repository;

import LeHuynhThuan.demo.entity.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVoucherRepository extends MongoRepository<Voucher, String> {

    Optional<Voucher> findByCodeIgnoreCase(String code);
}

