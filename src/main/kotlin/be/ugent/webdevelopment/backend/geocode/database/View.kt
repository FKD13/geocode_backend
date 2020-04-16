package be.ugent.webdevelopment.backend.geocode.database

interface View {
    /*
    * Model information that should never be passed by the api
    * */
    interface Ignore
    interface Id
    interface List : Id
    interface PublicDetail : List
    interface PrivateDetail : PublicDetail, PrettyPrivate
    interface AdminDetail : PublicDetail, PrettyPrivate
    interface PrettyPrivate : PublicDetail
}