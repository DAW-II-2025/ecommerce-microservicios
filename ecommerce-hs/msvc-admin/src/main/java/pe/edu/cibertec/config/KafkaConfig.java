package pe.edu.cibertec.config;

//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.postgresql.translation.messages_bg;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import pe.edu.cibertec.model.chat.Message;

//@Configuration
public class KafkaConfig {

//	  @Bean
//	    ConsumerFactory<String, Message> consumerFactory() {
//	        Map<String, Object> props = new HashMap<>();
//	        
//	        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//	        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificacion-group");
//	        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//	        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//	        
//	        // Propiedades específicas del JsonDeserializer
//	        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//	        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
//	        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "pe.edu.cibertec.model.chat.Message");
//	        
//	        return new DefaultKafkaConsumerFactory<>(props);
//	    }
//
//	@Bean
//	ConcurrentKafkaListenerContainerFactory<String, Message> articuloKafkaListenerContainerFactory() {
//		ConcurrentKafkaListenerContainerFactory<String, Message> factory =
//				new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		return factory;
//
//	}
}
