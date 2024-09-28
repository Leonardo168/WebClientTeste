package webClient.teste;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

	private final WebClient webClient;
	
	public PostService(WebClient.Builder builder) {
		webClient = builder.baseUrl("https://jsonplaceholder.typicode.com/").build();
	}
	
	public Flux<Post> findAll(){
		return webClient.get()
				.uri("/posts")
				.retrieve()
				.bodyToFlux(Post.class);
	}
	
	public Mono<Post> findById(Integer id){
		return webClient.get()
				.uri("/posts/{id}", id)
				.retrieve()
				.bodyToMono(Post.class);
	}
	
	public Mono<Post> create(Post post){
		return webClient.post()
				.uri("/posts")
				.bodyValue(post)
				.retrieve()
				.bodyToMono(Post.class);
	}
	
	
	public Mono<Post> update(Integer id, Post post){
		return webClient.put()
				.uri("/posts/{id}", id)
				.bodyValue(post)
				.retrieve()
				.bodyToMono(Post.class);
	}
	
	public Mono<Void> delete(Integer id){
		return webClient.delete()
				.uri("/posts/{id}", id)
				.retrieve()
				.bodyToMono(Void.class);
	}
}
