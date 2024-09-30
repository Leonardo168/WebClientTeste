package webClient.teste.dto;

public record User(long id, String name, String username, String email, Address address, String phone, String website,
		   Company company) {

}
