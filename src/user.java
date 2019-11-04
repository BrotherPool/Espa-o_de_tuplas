import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.util.Scanner;
public class user {
	
	
	public static ArrayList<Usuario> ListaUsuarios(JavaSpace space,String ambienteOndeSeEncontra) {
		Usuario templateUser=new Usuario();
		Usuario aux;
		int a=0,i=0;
		ArrayList<Usuario>listaObjetos=new ArrayList<Usuario>();
		ArrayList<Usuario>listaRetorno=new ArrayList<Usuario>();
		CountToAll templatCountUser=new CountToAll();
		templatCountUser.tipoCount="Usuario";
		try {
			CountToAll countUser = (CountToAll) space.read(templatCountUser, null, 60 * 1000);
			i = countUser.quantidade.intValue();
			for (a=0;a<i;a++) {
				aux=(Usuario) space.take(templateUser, null, 60 * 1000);
				listaObjetos.add(aux);
			}
			for (a=0;a<i;a++) {
				aux=(Usuario) listaObjetos.get(a);
				space.write(aux, null, Lease.FOREVER);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		for (a=0;a<i;a++) {
			if(listaObjetos.get(a).ambienteOndeSeEncontra.equals(ambienteOndeSeEncontra)) {
				listaRetorno.add(listaObjetos.get(a));
			}
		}
		return listaRetorno;
	}
	
	
	public static void AddUsuarioNoEspaco(JavaSpace space,String ambiente) {
		Usuario user=new Usuario();
		CountToAll templateCountUser=new CountToAll();
		templateCountUser.tipoCount="Usuario";
		user.ambienteOndeSeEncontra=ambiente;
		try {
			CountToAll countUser = (CountToAll) space.take(templateCountUser, null, 60 * 1000);
			//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
			user.nomeUsuario="user"+countUser.auxParaNome;
			countUser.auxParaNome+=1;
			//1 hora
			space.write(user, null, Lease.FOREVER);
			countUser.quantidade+=1;
			//System.out.println("Quantidade de Usuarios são: "+countUser.quantidade);
			space.write(countUser, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void RemoveUsuario(JavaSpace space,String nomeUsuario) {
		Usuario templateUser=new Usuario();
		templateUser.nomeUsuario=nomeUsuario;
		CountToAll templateCountUser=new CountToAll();
		templateCountUser.tipoCount="Usuario";
		try {
			CountToAll countUser = (CountToAll) space.take(templateCountUser, null, 60 * 1000);
			//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
			countUser.quantidade-=1;
			space.write(countUser, null, Lease.FOREVER);
			space.take(templateUser, null, 60 * 1000);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void UsuarioTrocaDeAmbiente(JavaSpace space,String nomeUsuario,String ambienteQueEleChega) {
		Usuario templateUser=new Usuario();
		templateUser.nomeUsuario=nomeUsuario;
		try {
			Usuario user = (Usuario) space.take(templateUser, null, 60 * 1000);
			user.ambienteOndeSeEncontra=ambienteQueEleChega;
			space.write(user, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}

	public static String UsuarioMandaMensagem(JavaSpace space,String usuarioQueMandaMensagem,String usuarioQueRecebeMensagem,String msg){
		Mensagem mensagemASerMandada=new Mensagem();
		mensagemASerMandada.mensagem=msg;
		mensagemASerMandada.quemEnviou=usuarioQueMandaMensagem;
		mensagemASerMandada.paraQuemEnviou=usuarioQueRecebeMensagem;
		try {
			space.write(mensagemASerMandada, null, Lease.FOREVER);
			return usuarioQueMandaMensagem+"): "+msg;
		} catch (Exception e) {
            e.printStackTrace();
        }
		return "Não foi possível enviar mensagem, tente novamente";
	}
	
	public static String UsuarioRecebeMensagem(JavaSpace space,String usuarioQueRecebeMensagem){
		Mensagem templateMensagem=new Mensagem();
		templateMensagem.paraQuemEnviou=usuarioQueRecebeMensagem;
		try {
			Mensagem mensagem = (Mensagem) space.take(templateMensagem, null, 60 * 1000);
			return mensagem.quemEnviou+"): "+mensagem.mensagem;
		} catch (Exception e) {
            e.printStackTrace();
        }
		return "";
	}
	
	public static void main(String[] args) {
		
	}
}
