package br.com.alura.loja.resource;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

@Path("carrinhos")
public class CarrinhoResource {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id) throws JAXBException{
		Optional<Carrinho> carrinho = new CarrinhoDAO().busca(id);
		return carrinho.orElse(null).toXML();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response adiciona(Carrinho carrinho) throws URISyntaxException{
		new CarrinhoDAO().adiciona(carrinho);
		URI uri = new URI("/carrinhos/"+carrinho.getId());
		
		return Response.created(uri).build();
	}
	
	@Path("{id}/produtos/{produtoId}")
	@DELETE
	public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId){
		Optional<Carrinho> carrinho = new CarrinhoDAO().busca(id);
		carrinho.get().remove(produtoId);
		return Response.ok().build();
	}
	
	@Path("{id}/produtos/{produtoId}/quantidade")
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response alteraProduto(Produto produto, @PathParam("id") long id, @PathParam("produtoId") long produtoId){
		Optional<Carrinho> carrinhoOpt = new CarrinhoDAO().busca(id);
		Carrinho carrinho = carrinhoOpt.get();
		carrinho.trocaQuantidade(produto);
		
		return Response.ok().build();
	}
}
