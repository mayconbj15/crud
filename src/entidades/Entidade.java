package entidades;

public interface Entidade
{
	/**
	 * Obtem o ID da entidade.
	 * 
	 * @return O ID da entidade.
	 */
	
	public int getId();
	
	/**
	 * Atribui um novo ID à entidade.
	 * 
	 * @param id Novo ID.
	 * 
	 * @return {@code id}.
	 */
	
	public int setId(int id);
	
	/**
	 * Método que mostra a Entidade com o nome da sua categoria
	 * 
	 * @return uma representação do objeto
	 */
	
	public String print();
	
	/**
	 * Método que obtém apenas as informações úteis para o usuário
	 * sobre a entidade.
	 * 
	 * @return uma representação da entidade para o usuário.
	 */
	
	public String printToUser();
}
