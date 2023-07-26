package br.com.multiinovacoes.gcon.controller;

import static br.com.multiinovacoes.gcon.controller.DescricaoEmailController.DESCRICAO_EMAIL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.multiinovacoes.gcon.model.dto.DescricaoEmailDto;
import br.com.multiinovacoes.gcon.model.mapper.DescricaoEmailMapper;
import br.com.multiinovacoes.gcon.model.request.DescricaoEmailRequest;
import br.com.multiinovacoes.gcon.model.response.DescricaoEmailResponse;
import br.com.multiinovacoes.gcon.repository.DescricaoEmailRepository;
import br.com.multiinovacoes.gcon.security.GconSecurity;
import br.com.multiinovacoes.gcon.service.DescricaoEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Api(value = "DescricaoEmail", produces = APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(path = DESCRICAO_EMAIL	)
public class DescricaoEmailController {
	
	public static final String DESCRICAO_EMAIL = "/descricaoEmail";
	public static final String LISTAR = "/listar";
	public static final String PARAMETRO = "/{codigo}";

	
	@Autowired
	DescricaoEmailRepository descricaoEmailRepository;
	
	@Autowired
	DescricaoEmailService descricaoEmailService;
	
	@Autowired
	DescricaoEmailMapper descricaoEmailMapper;
	
	@Autowired
	GconSecurity gconSecurity;


	@ApiOperation(value = "Obter uma descricao email para edição", nickname = DESCRICAO_EMAIL)
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public DescricaoEmailResponse getEditar() {
		return new DescricaoEmailResponse(descricaoEmailService.getDescricaoEmailOrgao(gconSecurity.getOrgao()));
	}

	
	@ApiOperation(value = "Obter uma descricao email para edição", nickname = PARAMETRO)
	@GetMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public DescricaoEmailResponse getEditarDescricaoEmail(@PathVariable Long codigo) {
		return new DescricaoEmailResponse(descricaoEmailService.getDescricaoEmail(codigo));
	}
	
	@ApiOperation(value = "Enviar uma descricao email", nickname = DESCRICAO_EMAIL)
	@PostMapping(produces = APPLICATION_JSON_VALUE)
	public DescricaoEmailResponse salvarDescricaoEmail(@Valid @RequestBody DescricaoEmailRequest request) {
		DescricaoEmailDto descricaoEmailDto = descricaoEmailMapper.fromDescricaoEmail(request);
		return new DescricaoEmailResponse(descricaoEmailService.salvarDescricaoEmail(descricaoEmailDto));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Excluir uma descricao email", nickname = PARAMETRO)
	@DeleteMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public void excluirDescricaoEmail(@PathVariable Long codigo) {
		descricaoEmailRepository.deleteById(codigo);
	}
	
	@ApiOperation(value = "Atualizar uma descricao email", nickname = PARAMETRO)
	@PutMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public DescricaoEmailResponse atualizarDescricaoEmail(@Valid @RequestBody DescricaoEmailDto request) {
		return new DescricaoEmailResponse(descricaoEmailService.salvarDescricaoEmail(request));
	}
	



}
