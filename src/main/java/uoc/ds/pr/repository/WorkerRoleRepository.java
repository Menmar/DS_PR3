package uoc.ds.pr.repository;

import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.BaseballCardsPR3;
import uoc.ds.pr.enums.WorkerRole;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.util.DSLinkedList;

public class WorkerRoleRepository {

  private final BaseballCardsPR3 baseballCards;
  private final HashTable<String, WorkerRole> workerRoles;
  private final Dictionary<WorkerRole, LinkedList<Worker>> workersByRole;
  private final WorkerRepository workerRepository;

  public WorkerRoleRepository(BaseballCardsPR3 baseballCards, WorkerRepository workerRepository) {
    this.baseballCards = baseballCards;
    this.workerRepository = workerRepository;
    this.workerRoles = new HashTable<>();
    this.workersByRole = new DictionaryAVLImpl<>();

    for (WorkerRole role : WorkerRole.values()) {
      workersByRole.put(role, new DSLinkedList<>(Worker.CMP_ID));
    }
  }

  public void addWorker(String workerId, WorkerRole role) {
    if (workerId == null || role == null) {
      return;
    }

    Worker worker = workerRepository.getWorker(workerId);
    if (worker == null) {
      return;
    }

    WorkerRole oldRole = workerRoles.get(workerId);
    if (oldRole != null && oldRole != role) {
      removeWorkerFromRoleList(worker, oldRole);
    }

    workerRoles.put(workerId, role);
    addWorkerToRoleList(worker, role);

  }

  private void addWorkerToRoleList(Worker worker, WorkerRole role) {
    LinkedList<Worker> wk = workersByRole.get(role);
    if (wk == null) {
      wk = new LinkedList<>();
      workersByRole.put(role, wk);
    }

    boolean found = false;
    Iterator<Worker> wkIt = wk.values();
    while (wkIt.hasNext() && !found) {
      found = wkIt.next().getId().equals(worker.getId());
    }

    if (!found) {
      wk.insertEnd(worker);
    }
  }

  public void removeWorkerFromRoleList(Worker worker, WorkerRole role) {
    Worker wk = worker.getWorker(new Worker(worker.getId(), worker.getName(), worker.getSurname(), role));
    if (wk != null) {
      removeWorkerFromRoleList(wk, role);
    }
  }


  public boolean hasRole(String workerId, WorkerRole role) {
    Worker worker= workerRepository.getWorker(workerId);
    if(worker == null) {
      return false;
    }
    WorkerRole assignedRole = workerRoles.get(workerId);
    return assignedRole == role;
  }

  public WorkerRole getRole(String workerId) {
    return workerRoles.get(workerId);
  }

  public Iterator<Worker> getWorkersByRole(WorkerRole role) {
    LinkedList<Worker> wk = workersByRole.get(role);
    if(wk == null  || wk.isEmpty()) {
      return new DSLinkedList<Worker>(Worker.CMP_ID).values();
    }
    return wk.values();
  }

  public boolean hasWorkerWithRole(WorkerRole role) {
    LinkedList<Worker> wk = workersByRole.get(role);
    return wk != null && !wk.isEmpty();
  }

  public void updateWorkerRole(Worker worker, WorkerRole role) {
    if (worker == null || role == null) {
      return;
    }
    String workerId = worker.getId();
    WorkerRole oldRole = workerRoles.get(workerId);
    if (oldRole != null) {
      if (oldRole == role) {
        return;
      }
      DSLinkedList<Worker> oldRoleWk = (DSLinkedList<Worker>) workersByRole.get(oldRole);
      if (oldRoleWk != null) {
        oldRoleWk.remove(worker);
      }
    }

    workerRoles.put(workerId, role);
    LinkedList<Worker> wk = workersByRole.get(role);
    boolean found = false;
    Iterator<Worker> wkIt = wk.values();
    while (wkIt.hasNext()) {
      if (wkIt.next().getId().equals(workerId)) {
        found = true;
        break;
      }
    }
    if (!found) {
      wk.insertEnd(worker);
    }
  }


}
