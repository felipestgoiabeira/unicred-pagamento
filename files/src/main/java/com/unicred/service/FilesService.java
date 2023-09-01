package com.unicred.service;


import com.unicred.domain.Ticket;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FilesService {

    void processBatch(MultipartFile file) throws IOException, BusinessException;

}
