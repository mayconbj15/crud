package user;

import crud.Arquivo;
import entidades.Entidade;
import util.IO;

public abstract class CrudAbstract<TIPO_ENTIDADE extends Entidade>
{
	protected Arquivo<TIPO_ENTIDADE> database;
	
	public CrudAbstract(Arquivo<TIPO_ENTIDADE> database)
	{
		this.database = database;
	}
	
	/**
	 * Insere uma entidade na base de dados.
	 * 
	 * @param entidade Entidade a ser inserida.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	public boolean inserir(TIPO_ENTIDADE entidade)
	{
		boolean success = false;
		
		if ( database.writeObject(entidade) )
		{
			IO.println("\nEntidade cadastrada com sucesso! :D\n");
			success = true;
		}
		
		else
		{
			IO.println("\nFalha no cadastramento da entidade.\n");
		}
		
		return success;
	}
	
	/**
	 * Altera a entidade com o id informado substituindo-a
	 * pela nova entidade informada.
	 * 
	 * @param id Id da entidade a ser alterada.
	 * @param entity Nova entidade a ser colocada no lugar.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	public boolean alterar(int id, TIPO_ENTIDADE entity)
	{
		boolean success = false;
		
		if (database.changeObject(id, entity))
		{
			IO.println("\nEntidade alterada com sucesso! :D\n");
			success = true;
		}
		
		else
		{
			IO.println("\nFalha na alteração da entidade.\n");
		}
		
		return success;
	}
	
	/**
	 * Exclui a entidade com o id informado.
	 * 
	 * @param id Id da entidade a ser excluída.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	public boolean excluir(int id)
	{
		boolean success = false;
		
		if (database.deleteObject(id))
		{
			IO.println("Entidade excluída com sucesso.");
			success = true;
		}
		
		else
		{
			IO.println("Falha na exclusão da entidade.");
		}
		
		return success;
	}
	
	/**
	 * Consulta a entidade com o id informado.
	 * 
	 * @param id Id da entidade a ser consultada.
	 * 
	 * @return {@code true} se tudo der certo;
	 * {@code false} caso contrário.
	 */
	
	public boolean consultar(int id)
	{
		boolean success = false;
		
		if (database.idIsValid(id))
		{
			TIPO_ENTIDADE entity= null;
			entity= database.readObject(id);
			
			if(entity != null){
				IO.println( "\n" + entity);
				success = true;
			}
			else{
				IO.println("Categoria vazia");
			}
				
		}
		
		else
		{
			IO.println("Id inválido!");
		}
		
		return success;
	}
	
	/**
	 * Lista todas as entidades ativas da base de dados.
	 */
	
	public void listar()
	{
		database.list().forEach( (entidade) -> { IO.println(entidade + "\n"); } );
	}
}
