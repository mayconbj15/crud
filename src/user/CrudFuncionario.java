package user;

import crud.Arquivo;
import entidades.Funcionario;

/**
 * Classe que gerencia a interação com o usuário.
 */

public class CrudFuncionario extends CrudAbstract<Funcionario>
{
	public CrudFuncionario(Arquivo<Funcionario> database)
	{
		super(database);
	}
}