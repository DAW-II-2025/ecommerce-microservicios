package pe.edu.cibertec.service;

import pe.edu.cibertec.model.entity.Message;

public interface IMessageNotificationService {
	void envioNotificacion(Message mensaje);
}
