import { FeatureViewState } from '../models/view-state.models';

export function createFeatureViewState<T>(size = 5): FeatureViewState<T> {
  return {
    loading: false,
    errorMessage: null,
    items: [],
    page: 0,
    size,
    totalPages: 0,
  };
}

export function startLoading<T>(state: FeatureViewState<T>): FeatureViewState<T> {
  return {
    ...state,
    loading: true,
    errorMessage: null,
  };
}

export function setLoaded<T>(
  state: FeatureViewState<T>,
  payload: Pick<FeatureViewState<T>, 'items' | 'page' | 'totalPages'>
): FeatureViewState<T> {
  return {
    ...state,
    loading: false,
    items: payload.items,
    page: payload.page,
    totalPages: payload.totalPages,
  };
}

export function setError<T>(state: FeatureViewState<T>, errorMessage: string): FeatureViewState<T> {
  return {
    ...state,
    loading: false,
    errorMessage,
  };
}
