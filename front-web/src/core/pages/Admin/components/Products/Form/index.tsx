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
    const { register, handleSubmit, errors } = useForm<FormState>()

    const onSubmit = (data: FormState) => {
        makePrivateRequest({ url: '/products', method: 'POST', data })
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm title="casdastrar um produto">
                <div className="row">
                    <div className="col-6">
                        <div className="margin-bottom-30">
                            <input
                                name='name'
                                type='text'
                                className='form-control  input-base'
                                placeholder='Nome do Produto'
                                ref={register({
                                    required: "Campo obrigatório",
                                    minLength:{value: 5, message: 'O campo deve ter no mínimo 5 caracteres'},
                                    maxLength:{value:60, message:'O campo deve ter no máximo 60 caracteres'} })} />
                            {errors.name && (
                                <div className="invalid-feedback d-block">
                                    {errors.name.message}
                                </div>)}
                        </div>

                        <div className="margin-bottom-30">
                            <input
                                name='price'
                                type='number'
                                className='form-control input-base'
                                placeholder="Preço"
                                ref={register({ required: "Campo obrigatório" })} />
                                {errors.price && (
                                <div className="invalid-feedback d-block">
                                    {errors.price.message}
                                </div>)}
                        </div>

                        <div className="margin-bottom-30">
                            <input
                                name='imgUrl'
                                type='text'
                                className='form-control  input-base'
                                placeholder='Imagem do Produto'
                                ref={register({ required: "Campo obrigatório" })} />
                                {errors.imgUrl && (
                                <div className="invalid-feedback d-block">
                                    {errors.imgUrl.message}
                                </div>)}
                        </div>
                    </div>
                    <div className="col-6">
                        <textarea
                            name='description'
                            className='form-control input-base'
                            placeholder='Descrição'
                            cols={30}
                            rows={10}
                            ref={register({ required: "Campo obrigatório" })} />
                            {errors.description && (
                                <div className="invalid-feedback d-block">
                                    {errors.description.message}
                                </div>)}
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}
export default Form;