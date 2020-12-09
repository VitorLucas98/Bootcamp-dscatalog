import { makePrivateRequest, makeRequest } from 'core/utils/request';
import Select from 'react-select'
import { toast } from 'react-toastify';
import React, { useEffect, useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import BaseForm from '../../BaseForm';
import './styles.scss';
import { useHistory, useParams } from 'react-router-dom';
import { Category } from 'core/types/Product';

type FormState = {
    name: string;
    price: string;
    description: string;
    categories: Category[];
    imgUrl: string;
}
type ParamsType = {
    productId: string;
}

const Form = () => {
    const { productId } = useParams<ParamsType>();
    const [categories, setCategories] = useState<Category[]>([]);
    const [isLoadingCategories, setIsLoadingCategories] = useState(false);
    const { register, handleSubmit, errors, setValue, control } = useForm<FormState>()
    const history = useHistory();
    const isEditing = productId !== 'create';
    useEffect(() => {
        if (isEditing) {
            makeRequest({ url: `/products/${productId}` })
                .then(response => {
                    setValue('name', response.data.name);
                    setValue('price', response.data.price);
                    setValue('description', response.data.description);
                    setValue('imgUrl', response.data.imgUrl);
                    setValue('categories', response.data.categories);
                })
        }
    }, [productId, isEditing, setValue])
    useEffect(() => {
        setIsLoadingCategories(true)
        makeRequest({ url: '/categories' })
            .then(response => setCategories(response.data.content))
            .finally(() => {
                setIsLoadingCategories(false)
            })
    }, [])
    const onSubmit = (data: FormState) => {
        makePrivateRequest({
            url: isEditing ? `/products/${productId}` : '/products',
            method: isEditing ? 'PUT' : 'POST',
            data
        })
            .then(() => {
                toast.info('Produto salvo com sucesso!')
                history.push('/admin/products')
            }).catch(() => {
                toast.error('Erro ao salvar produto!')
            })
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm
                title={isEditing ? 'Editar Produto' : "Cadastrar um Produto"}>
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
                                    minLength: { value: 5, message: 'O campo deve ter no mínimo 5 caracteres' },
                                    maxLength: { value: 60, message: 'O campo deve ter no máximo 60 caracteres' }
                                })} />
                            {errors.name && (
                                <div className="invalid-feedback d-block">
                                    {errors.name.message}
                                </div>)}
                        </div>
                        <div className="margin-bottom-30">
                            <Controller
                                as={Select}
                                name='categories'
                                rules={{ required: true }}
                                control={control}
                                options={categories}
                                isLoading={isLoadingCategories}
                                getOptionLabel={(option: Category) => option.name}
                                getOptionValue={(option: Category) => String(option.id)}
                                classNamePrefix='categories-select'
                                isMulti
                                placeholder='Categorias'
                            />
                             {errors.categories && (
                                <div className="invalid-feedback d-block">
                                   Campo obrigatório
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