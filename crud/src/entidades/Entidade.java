package entidades;

public interface Entidade {
	
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
	 * Gera um arranjo de bytes com os campos internos da entidade.
	 * 
	 * @return Arranjo de bytes com os campos internos da entidade.
	 */
	
	public int getIdCategoria();
	
	public int setIdCategoria(int id);
	
	/**
	 * Método que mostra a Entidade com o nome da sua categoria
	 * @return uma representação do objeto
	 */
	public String print();
}
