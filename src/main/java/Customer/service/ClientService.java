package Customer.service;

import Customer.dto.client.ClientDTO;
import Customer.dto.ResponseData;
import Customer.dto.client.UserDniDTO;
import Customer.dto.client.UserIdDTO;
import Customer.exceptions.CustomValidationException;

import java.util.List;
import java.util.Map;

public interface ClientService {

    List<ClientDTO> getAllClients();
    ClientDTO getUserById(UserIdDTO userIdDTO) throws CustomValidationException;
    ResponseData saveClient(ClientDTO clientDTO) throws CustomValidationException;
    ResponseData updateClient(Integer userId, Map<String, Object> fields) throws CustomValidationException;
    ResponseData deleteClient(UserDniDTO userDniDTO) throws CustomValidationException;
    ResponseData updateClientAllData(ClientDTO clientDTO) throws CustomValidationException;

}
