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
	
	public boolean alterar(int id, TIPO_ENTIDADE item)
	{
		boolean success = false;
		
		if (database.changeObject(id, item))
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
	
	public boolean consultar(int id)
	{
		boolean success = false;
		
		if (database.idIsValid(id))
		{
			IO.println( "\n" + database.readObject(id) );
			success = true;
		}
		
		else
		{
			IO.println("Id inválido!");
		}
		
		return success;
	}
	
	public void listar()
	{
		database.list().forEach( (entidade) -> { IO.println(entidade + "\n"); } );
	}
}
