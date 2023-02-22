package com.my.app.controller;

import com.epam.app.exception.ObjectNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQLApiExceptionHandler extends DataFetcherExceptionResolverAdapter {
    private static final String OBJECT_NOT_FOUND_MESSAGE_EXCEPTION = "Object is not found!";

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ObjectNotFoundException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(OBJECT_NOT_FOUND_MESSAGE_EXCEPTION + " " + ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        return super.resolveToSingleError(ex, env);
    }
}
