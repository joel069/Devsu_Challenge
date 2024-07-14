package Customer.service.impl;

import Customer.dto.ResponseData;
import Customer.dto.client.ClientDTO;
import Customer.dto.client.UserDniDTO;
import Customer.dto.client.UserIdDTO;
import Customer.enums.SuccessCodesEnum;
import Customer.enums.SuccessMessagesEnum;
import Customer.exceptions.CustomValidationException;
import Customer.exceptions.GlobalExceptionHandler;
import Customer.model.T_Client;
import Customer.model.T_Person;
import Customer.repository.ClientRepository;
import Customer.repository.PersonRepository;
import Customer.service.ClientService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ClientServiceImpl(ClientRepository clientRepository,
                             PersonRepository personRepository
                             ) {
        this.clientRepository = clientRepository;
        this.personRepository = personRepository;
    }

    public List<ClientDTO> getAllClients() {
        List<T_Client> clients = clientRepository.findAll();

        return clients.stream()
                .map(client -> {
                    ClientDTO clientDTO = new ClientDTO();
                    modelMapper.map(client, clientDTO);
                    return clientDTO;
                })
                .collect(Collectors.toList());
    }


    public ClientDTO getUserById(UserIdDTO userIdDTO) throws CustomValidationException {
        Optional<T_Client> user = clientRepository.findById(userIdDTO.getUserId());

        if (user.isPresent()) {
            T_Client userFound = user.get(); // Assuming user is found
            ClientDTO clientDTO = new ClientDTO();
            modelMapper.map(userFound, clientDTO);
            return clientDTO;
        } else {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
    }

    @Transactional
    public synchronized ResponseData saveClient(ClientDTO clientDTO) throws CustomValidationException {

        Optional<T_Person> existingPerson = personRepository.findByPersonDni(clientDTO.getPersonDni());
        if (existingPerson.isPresent()) {
            throw GlobalExceptionHandler.clientAlreadyExistException();
        }
        T_Client newClient = new T_Client();
        modelMapper.map(clientDTO, newClient);

        clientRepository.save(newClient);

        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_CREATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData updateClient(Integer userId, Map<String, Object> fields) throws CustomValidationException {

        Optional<T_Client> client = clientRepository.findById(userId);
        if (client.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(T_Client.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, client.get(), value);
            } else {
                throw GlobalExceptionHandler.fieldNotFoundException(key);
            }
        });
        clientRepository.save(client.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );

    }

    public ResponseData updateClientAllData(ClientDTO clientDTO) throws CustomValidationException {
        Optional<T_Person> existingPersonOpt = personRepository.findByPersonDni(clientDTO.getPersonDni());

        if (existingPersonOpt.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }

        T_Person existingPerson = existingPersonOpt.get();
        existingPerson.setPersonName(clientDTO.getPersonName());
        existingPerson.setPersonGenre(clientDTO.getPersonGenre());
        existingPerson.setPersonAge(clientDTO.getPersonAge());
        existingPerson.setPersonAddress(clientDTO.getPersonAddress());
        existingPerson.setPersonPhoneNumber(clientDTO.getPersonPhoneNumber());

        if (existingPerson instanceof T_Client) {
            T_Client existingClient = (T_Client) existingPerson;
            existingClient.setClientState(clientDTO.getClientState());
            existingClient.setClientPassword(clientDTO.getClientPassword());
        } else {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        personRepository.save(existingPerson);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData deleteClient(UserDniDTO userDniDTO) throws CustomValidationException {
        Optional<T_Person> client = personRepository.findByPersonDni(userDniDTO.getUserDni());
        if (client.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        personRepository.delete(client.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }


}
