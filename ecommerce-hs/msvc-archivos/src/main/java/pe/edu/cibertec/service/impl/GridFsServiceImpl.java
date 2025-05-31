package pe.edu.cibertec.service.impl;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import pe.edu.cibertec.service.GridFsService;
	
@Service
public class GridFsServiceImpl implements GridFsService {
    private final GridFsTemplate gridFsTemplate;

    public GridFsServiceImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public String saveFile(MultipartFile file) throws IOException {    
    		ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
    		return id.toString();	
    }

  
    public GridFSFile getFile(String id) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
    }

    @Override
    public InputStreamResource downloadFile(String id) throws IOException {
        GridFSFile gridFsFile = getFile(id);
        GridFsResource resource = gridFsTemplate.getResource(gridFsFile);
        return new InputStreamResource(resource.getInputStream());
    }
}
