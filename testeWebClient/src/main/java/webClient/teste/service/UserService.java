package webClient.teste.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webClient.teste.dto.User;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder builder) {
	webClient = builder.baseUrl("https://jsonplaceholder.typicode.com/").build();
//	webClient = builder.baseUrl("https://webhook.site/32a8bcb4-bcaf-43f6-823c-e63a92e5d8ef").build();
    }

    public Flux<User> findAll() {
	return webClient.get()
			.uri("/users")
			.retrieve()
			.bodyToFlux(User.class);
    }

    public Mono<User> findById(Integer id) {
	return webClient.get()
			.uri("/users/{id}", id)
			.retrieve()
			.bodyToMono(User.class);
    }

    public Mono<User> create(User user) {
	return webClient.post()
			.uri("/users")
			.bodyValue(user)
			.retrieve()
			.bodyToMono(User.class);
    }

    public Mono<User> update(Integer id, User user) {
	return webClient.put()
			.uri("/users/{id}", id)
			.bodyValue(user)
			.retrieve()
			.bodyToMono(User.class);
    }

    public Mono<Void> delete(Integer id) {
	return webClient.delete()
			.uri("/users/{id}", id)
			.retrieve()
			.bodyToMono(Void.class);
    }
}
