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
	 * Obtem o ID de outra entidade que está relacionada com esta.
	 * 
	 * @return o ID de outra entidade que está relacionada com esta.
	 */
	
	public int getIdSecundario();
	
	/**
	 * Método que mostra a Entidade com o nome da sua categoria
	 * 
	 * @return uma representação do objeto
	 */
	
	public String print();
}
