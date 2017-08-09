class MainActivityModel
    @Inject constructor(
            private var noteDAO: NoteDAO,
            private var userDAO: UserDAO
    ): ViewModel(), AnkoLogger {

    private var notesData: LiveData<List<Note>>? = null

    fun getNotes( ): LiveData<List<Note>>? {
        return notesData
    }

    fun subscribeToNotesDBChanges( callback: OnSync ) {
        doAsync {
            notesData = noteDAO.findAllNotedObservable()
            info("Notes received.")
            callback.notesReceived()

        }
    }

    public interface OnSync {
        fun notesReceived( )
    }

}