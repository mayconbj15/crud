package gerenciadores;

public interface Autenticavel
{
	/**
	 * Obtem o nome da entidade autenticável.
	 * 
	 * @return o nome da entidade autenticável.
	 */
	
	String getNome();
	
	/**
	 * Obtem o email da entidade autenticável.
	 * 
	 * @return o email da entidade autenticável.
	 */
	
	String getEmail();
	
	/**
	 * Obtem a senha da entidade autenticável.
	 * 
	 * @return a senha da entidade autenticável.
	 */
	
	String getSenha();
	
	/**
	 * Lê o nome da entidade autenticável e faz um set interno
	 * desse campo.
	 * 
	 * @return o nome da entidade autenticável.
	 */
	
	String readNome();
	
	/**
	 * Lê o email da entidade autenticável e faz um set interno
	 * desse campo.
	 * 
	 * @return o email da entidade autenticável.
	 */
	
	String readEmail();
	
	/**
	 * Lê a senha da entidade autenticável e faz um set interno
	 * desse campo.
	 * 
	 * @return a senha da entidade autenticável.
	 */
	
	String readSenha();
}
