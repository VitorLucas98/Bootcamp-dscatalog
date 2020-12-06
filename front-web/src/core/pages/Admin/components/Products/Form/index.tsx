import { makePrivateRequest } from 'core/utils/request';
import React from 'react';
import { useForm } from 'react-hook-form';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    description: string;
    imgUrl: string;
}

const Form = () => {
    const { register, handleSubmit,  } = useForm<FormState>()

    const onSubmit = (data: FormState) => {
        makePrivateRequest({ url: '/products', method: 'POST', data})
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm title="casdastrar um produto">
                <div className="row">
                    <div className="col-6">
                        <input
                            name='name'
                            type='text'
                            className='form-control margin-bottom-30 input-base'
                            placeholder='Nome do Produto' 
                            ref={register({ required: "Campo obrigatório" })}/>

                        <input
                            name='price'
                            type='number'
                            className='form-control margin-bottom-30 input-base'
                            placeholder="Preço" 
                            ref={register({ required: "Campo obrigatório" })}/>

                        <input
                            name='imgUrl'
                            type='text'
                            className='form-control margin-bottom-30 input-base'
                            placeholder='Imagem do Produto' 
                            ref={register({ required: "Campo obrigatório" })}/>

                    </div>
                    <div className="col-6">
                        <textarea
                            name='description'
                            className='form-control input-base'
                            placeholder='Descrição'
                            cols={30}
                            rows={10} 
                            ref={register({ required: "Campo obrigatório" })}/>
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}
export default Form;