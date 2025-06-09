package pe.edu.cibertec.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.model.entity.Conversation;

@Repository
public interface IConverationRepository extends MongoRepository<Conversation, String>{

	@Query(value = "{}", fields = "{ 'messages': 1, '_id': 1 }" )
	List<Conversation> findAllConversations();

}
