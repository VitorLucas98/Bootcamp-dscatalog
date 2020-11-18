import React from 'react';
import AuthCard from '../Card';
import './styles.scss';
import { useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import ButtonIcon from 'core/components/ButtonIcon';

type FormData = {
    email: string;
    password:string;
}

const Login = () => {
    const { register, handleSubmit} = useForm<FormData>();
    const onSubmit = (data:FormData) => {
        console.log(data);
      };
    return (
        <AuthCard title='login'>
            <form className='login-form' onSubmit={handleSubmit(onSubmit)}>
                <input type="email"
                    className='input-base form-control margin-bottom-30'
                    placeholder='Email'
                    name="email" ref={register}  />
                <input type="password"
                    className='input-base form-control'
                    placeholder='Senha' 
                    name="password" ref={register} />
                <Link to='/admin/auth/recover' className='login-link-recover'>
                    Esqueci a senha ?
                </Link>
                <div className='login-submit'>
                    <ButtonIcon text='logar' />
                </div>
                <div className='text-center'>
                    <span className='not-registered'>Não tem Cadastro?</span>
                    <Link to='/admin/auth/register' className='login-link-register'>
                        CADASTRAR
                    </Link>
                </div>
            </form>
        </AuthCard>
    );
}

export default Login;

