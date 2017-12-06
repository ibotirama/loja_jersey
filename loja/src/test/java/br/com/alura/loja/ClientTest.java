package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;
import br.com.alura.loja.modelo.Servidor;
import junit.framework.Assert;

public class ClientTest {

	private HttpServer server;

	@Before
	public void startServer(){
	    server = Servidor.inicializaServidor();	
	}
	
    @After
    public void mataServidor() {
    	Servidor.paraServidor(server);
    }
    

	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado(){
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target("http://localhost:8080");
		Carrinho carrinho = target.path("/carrinhos/1").request().get(Carrinho.class);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());

	}
	
	@Test
	public void testaSeOConseguePesquisarOProjetoEsperado(){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = new Gson().fromJson(conteudo, Projeto.class);
		Assert.assertEquals(1L, projeto.getId(), 0);
	}
	
	@Test
	public void verificaSeConsegueAdicionarUmCarrinho(){
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

        Response response = target.path("/carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
	}
}
