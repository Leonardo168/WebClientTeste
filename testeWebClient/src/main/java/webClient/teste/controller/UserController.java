package webClient.teste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webClient.teste.dto.Address;
import webClient.teste.dto.Company;
import webClient.teste.dto.Geo;
import webClient.teste.dto.User;
import webClient.teste.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Flux<User>> getAll() {
	Flux<User> users = userService.findAll();
	users.subscribe(System.out::println);

	return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<User>> getById(@PathVariable Integer id) {
	Mono<User> user = userService.findById(id);
	user.subscribe(u -> {
	    System.out.println("Id: " + u.id());
	    System.out.println("username: " + u.username());
	});

	return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping
    public ResponseEntity<Mono<User>> postMethodName() {
	User userObj = new User(
				11,
				"nameTeste",
				"usernameTeste",
				"emailTeste",
				new Address(
					    "streetTeste",
					    "suiteTeste",
					    "cityTeste",
					    "zipcodeTeste",
					    new Geo(
						    "latTeste",
						    "lngTeste")),
				"phoneTeste",
				"websiteTeste",
				new Company(
					    "nameTeste",
					    "catchPhraseTeste",
					    "bsTeste"));

	Mono<User> user = userService.create(userObj);
	user.subscribe(System.out::println);

	return ResponseEntity.status(HttpStatus.OK).body(user);
    }

	@PutMapping("/{id}")
	public Mono<ResponseEntity<String>> putMethodName(@PathVariable Integer id) {
	    User userObj = new User(
					1,
					"nameTeste",
					"usernameTeste",
					"emailTeste",
					new Address(
						    "streetTeste",
						    "suiteTeste",
						    "cityTeste",
						    "zipcodeTeste",
						    new Geo(
							    "latTeste",
							    "lngTeste")),
					"phoneTeste",
					"websiteTeste",
					new Company(
						    "nameTeste",
						    "catchPhraseTeste",
						    "bsTeste"));
		
		return userService.findById(id)
		        .flatMap(user -> userService.update(id, userObj)
		            .then(Mono.just(ResponseEntity.status(HttpStatus.OK).body("Updated post id: " + user.id()))))
		        .onErrorResume(e ->{
		        	if (e instanceof WebClientResponseException.NotFound) {
		                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
		            } else {
		                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));
		            }
		        });
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<String>> deleteMethodName(@PathVariable Integer id) {
		return userService.findById(id)
		        .flatMap(user -> userService.delete(id)
		            .then(Mono.just(ResponseEntity.status(HttpStatus.OK).body("Deleted post id: " + user.id()))))
		        .onErrorResume(e ->{
		        	if (e instanceof WebClientResponseException.NotFound) {
		                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
		            } else {
		                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));
		            }
		        });
	}

}
