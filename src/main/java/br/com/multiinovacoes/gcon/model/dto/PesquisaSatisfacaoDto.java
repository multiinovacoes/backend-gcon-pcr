package br.com.multiinovacoes.gcon.model.dto;

public class PesquisaSatisfacaoDto {
	
	private Long idPergunta;
	
	private Long idResposta;
	

	public Long getIdPergunta() {
		return idPergunta;
	}

	public void setIdPergunta(Long idPergunta) {
		this.idPergunta = idPergunta;
	}

	public Long getIdResposta() {
		return idResposta;
	}

	public void setIdResposta(Long idResposta) {
		this.idResposta = idResposta;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPergunta == null) ? 0 : idPergunta.hashCode());
		result = prime * result + ((idResposta == null) ? 0 : idResposta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PesquisaSatisfacaoDto other = (PesquisaSatisfacaoDto) obj;
		if (idPergunta == null) {
			if (other.idPergunta != null)
				return false;
		} else if (!idPergunta.equals(other.idPergunta))
			return false;
		if (idResposta == null) {
			if (other.idResposta != null)
				return false;
		} else if (!idResposta.equals(other.idResposta))
			return false;
		return true;
	}


	
}
