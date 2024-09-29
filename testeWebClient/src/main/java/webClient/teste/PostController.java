package webClient.teste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	PostService postService;
	
	@GetMapping
	public ResponseEntity<Flux<Post>> getAll() {
		Flux<Post> posts = postService.findAll();
		posts.subscribe(System.out::println);

		return ResponseEntity.status(HttpStatus.OK).body(posts);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Mono<Post>> getById(@PathVariable Integer id) {
		Mono<Post> post = postService.findById(id);
		post.subscribe(p -> {
			System.out.println("userId: " + p.userId());
			System.out.println("Id: " + p.id());
			System.out.println("title: " + p.title());
			System.out.println("body: " + p.body());
		});

		return ResponseEntity.status(HttpStatus.OK).body(post);
	}

	@PostMapping
	public ResponseEntity<Mono<Post>> postMethodName() {
		Post postObj = new Post(1, 101, "titleTeste", "bodyTeste");

		Mono<Post> post = postService.create(postObj);
		post.subscribe(System.out::println);

		return ResponseEntity.status(HttpStatus.OK).body(post);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Mono<Post>> putMethodName(@PathVariable Integer id) {
		Post postObj = new Post(1, id, "titleTeste", "bodyTeste");

		Mono<Post> post = postService.update(id, postObj);
		post.subscribe(System.out::println);

		return ResponseEntity.status(HttpStatus.OK).body(post);
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<String>> deleteMethodName(@PathVariable Integer id) {
		return postService.findById(id)
		        .flatMap(post -> postService.delete(id)
		            .then(Mono.just(ResponseEntity.status(HttpStatus.OK).body("Deleted post id: " + post.id()))))
		        .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found")))
		        .onErrorResume(e ->{
		        	if (e instanceof WebClientResponseException.NotFound) {
		                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
		            } else {
		                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));
		            }
		        });
	}
}
