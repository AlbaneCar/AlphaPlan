import { customFetch } from '../utils/FetchUtil';


export async function fetchRoles() {
    const response = await customFetch('roles');
    return await response.json();
}
