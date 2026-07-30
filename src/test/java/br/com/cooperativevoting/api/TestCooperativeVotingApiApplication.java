package br.com.cooperativevoting.api;

import org.springframework.boot.SpringApplication;

public class TestCooperativeVotingApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CooperativeVotingApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
