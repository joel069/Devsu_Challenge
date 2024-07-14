package Customer.controller;

import Customer.dto.*;
import Customer.dto.client.ClientDTO;
import Customer.dto.client.UserDniDTO;
import Customer.dto.client.UserIdDTO;
import Customer.exceptions.CustomValidationException;
import Customer.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devsu/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/findAllClients")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/findClientById")
    public ResponseEntity<ClientDTO> getCompanyById(@RequestBody @Valid UserIdDTO userIdDTO) throws CustomValidationException {
        ClientDTO client = clientService.getUserById(userIdDTO);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/saveClient")
    public ResponseEntity<ResponseData> saveClient(@RequestBody @Valid ClientDTO clientDTO) throws CustomValidationException {
        ResponseData client = clientService.saveClient(clientDTO);
        return ResponseEntity.ok(client);
    }

    @PatchMapping("/updateClient/{userId}")
    public ResponseEntity<ResponseData> updateClient(@PathVariable Integer userId, @RequestBody Map<String, Object> fields) throws CustomValidationException {
        ResponseData client = clientService.updateClient(userId, fields);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/updateClientAllData")
    public ResponseEntity<ResponseData> updateClientAllData(@RequestBody @Valid ClientDTO clientDTO) throws CustomValidationException {
        ResponseData client = clientService.updateClientAllData(clientDTO);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/deleteClient")
    public ResponseEntity<ResponseData> deleteClient(@RequestBody @Valid UserDniDTO userDniDTO) throws CustomValidationException {
        ResponseData client = clientService.deleteClient(userDniDTO);
        return ResponseEntity.ok(client);
    }



}
