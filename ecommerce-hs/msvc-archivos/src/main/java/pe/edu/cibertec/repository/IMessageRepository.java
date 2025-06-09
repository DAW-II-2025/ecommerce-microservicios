package pe.edu.cibertec.repository;

import org.apache.logging.log4j.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessageRepository extends MongoRepository<Message, String>{

}
